package com.example.HottiMaze.service;

import com.example.HottiMaze.entity.User;
import com.example.HottiMaze.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    /**
     * 00시(자정)에 호출되어야 하는 메서드.
     * 모든 사용자의 isAvailableChulcheck를 1로 초기화함.
     */
    @Transactional
    public void resetCheckInAvailability() {
        List<User> all = userRepository.findAll();
        for (User u : all) {
            u.setIsAvailableChulcheck(1);
        }
        userRepository.saveAll(all);
    }

    /**
     * 프론트에서 출석 체크 버튼을 누르면 호출.
     * username을 기준으로 사용자 조회 →
     * isAvailableChulcheck == 1인지 확인 →
     * chulcheck +1, point +50, isAvailableChulcheck = -1 → 저장.
     *
     * @param username 현재 로그인한 사용자의 username
     * @throws IllegalStateException: 이미 출석했거나, 사용자를 찾을 수 없을 때 던짐
     */
    @Transactional
    public void doCheckIn(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다. username: " + username));

        if (user.getIsAvailableChulcheck() == null || user.getIsAvailableChulcheck() != 1) {
            throw new IllegalStateException("오늘 이미 출석을 완료하였거나, 출석 가능 상태가 아닙니다.");
        }

        user.setChulcheck(user.getChulcheck() + 1);
        user.setPoint(user.getPoint() + 50);
        user.setIsAvailableChulcheck(-1);

        userRepository.save(user);
    }

    @Transactional
    public void registerUser(String username, String rawPassword) {
        if(userRepository.findByUsername(username).isPresent()){
            throw new IllegalStateException("이미 존재하는 아이디 입니다.");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setPoint(0);
        user.setChulcheck(0);
        user.setIsAvailableChulcheck(1);

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getUserEntityByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다: " + username));
    }
}
