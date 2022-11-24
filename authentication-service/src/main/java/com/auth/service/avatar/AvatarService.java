package com.auth.service.avatar;

import com.auth.entity.Avatar;

public interface AvatarService {

    Avatar create(Avatar user);

    Avatar get(Integer id);

    void delete(Integer id);
}
