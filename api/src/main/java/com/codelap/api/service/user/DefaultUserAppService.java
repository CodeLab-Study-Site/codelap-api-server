package com.codelap.api.service.user;

import com.codelap.common.user.domain.*;
import com.codelap.integration.s3.FileUpload;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultUserAppService implements UserAppService {

    private final UserRepository userRepository;
    private final UserQueryAppService userQueryAppService;
    private final FileUpload fileUpload;

    @Override
    public boolean getDuplicateCheckByName(String name) {
        return userQueryAppService.getDuplicateCheckByName(name);
    }

    @Override
    public boolean isActivated(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        return user.isActivated();
    }

    @Override
    public void update(Long userId, String name, UserCareer career, List<UserTechStack> techStacks, MultipartFile image) throws IOException {
        User user = userRepository.findById(userId).orElseThrow();

        if (!image.isEmpty()) {
            UserFile file = (UserFile) fileUpload.upload(image, "user", new UserFile());
            user.update(name, career, techStacks, List.of(file));
        } else {
            user.update(name, career, techStacks);
        }
    }
}
