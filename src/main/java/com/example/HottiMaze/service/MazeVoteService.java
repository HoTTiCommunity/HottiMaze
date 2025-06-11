package com.example.HottiMaze.service;

import com.example.HottiMaze.dto.MazeVoteDto;
import com.example.HottiMaze.dto.MazeVoteStatsDto;
import com.example.HottiMaze.entity.Maze;
import com.example.HottiMaze.entity.MazeVote;
import com.example.HottiMaze.entity.User;
import com.example.HottiMaze.repository.MazeRepository;
import com.example.HottiMaze.repository.MazeVoteRepository;
import com.example.HottiMaze.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MazeVoteService {

    private final MazeVoteRepository mazeVoteRepository;
    private final MazeRepository mazeRepository;
    private final UserRepository userRepository;

    /**
     * 미로에 투표하기 (좋아요/싫어요)
     * @param username 투표하는 사용자명
     * @param voteDto 투표 정보 (미로 ID, 좋아요 여부)
     * @return 투표 결과 통계
     */
    @Transactional
    public MazeVoteStatsDto voteMaze(String username, MazeVoteDto voteDto) {
        // 사용자 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다: " + username));

        // 미로 조회
        Maze maze = mazeRepository.findById(voteDto.getMazeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 미로입니다: " + voteDto.getMazeId()));

        // 기존 투표 확인
        Optional<MazeVote> existingVote = mazeVoteRepository.findByMazeIdAndUserId(
                voteDto.getMazeId(), user.getId());

        if (existingVote.isPresent()) {
            // 기존 투표가 있으면 업데이트
            MazeVote vote = existingVote.get();

            // 같은 투표면 취소 (삭제)
            if (vote.getIsLike().equals(voteDto.getIsLike())) {
                mazeVoteRepository.delete(vote);
            } else {
                // 다른 투표면 변경
                vote.setIsLike(voteDto.getIsLike());
                mazeVoteRepository.save(vote);
            }
        } else {
            // 새로운 투표 생성
            MazeVote newVote = new MazeVote();
            newVote.setMaze(maze);
            newVote.setUser(user);
            newVote.setIsLike(voteDto.getIsLike());
            mazeVoteRepository.save(newVote);
        }

        // 투표 후 통계 반환
        return getMazeVoteStats(voteDto.getMazeId(), username);
    }

    /**
     * 미로의 투표 통계 조회
     * @param mazeId 미로 ID
     * @param username 현재 사용자명 (null 가능)
     * @return 투표 통계
     */
    @Transactional(readOnly = true)
    public MazeVoteStatsDto getMazeVoteStats(Long mazeId, String username) {
        // 미로 존재 확인
        if (!mazeRepository.existsById(mazeId)) {
            throw new IllegalArgumentException("존재하지 않는 미로입니다: " + mazeId);
        }

        // 투표 통계 조회
        long likeCount = mazeVoteRepository.countLikesByMazeId(mazeId);
        long dislikeCount = mazeVoteRepository.countDislikesByMazeId(mazeId);
        long totalVotes = likeCount + dislikeCount;

        // 현재 사용자의 투표 상태 확인
        Boolean userVote = null;
        if (username != null) {
            Optional<User> user = userRepository.findByUsername(username);
            if (user.isPresent()) {
                Optional<MazeVote> userVoteEntity = mazeVoteRepository.findByMazeIdAndUserId(
                        mazeId, user.get().getId());
                if (userVoteEntity.isPresent()) {
                    userVote = userVoteEntity.get().getIsLike();
                }
            }
        }

        // 통계 DTO 생성
        MazeVoteStatsDto stats = new MazeVoteStatsDto();
        stats.setMazeId(mazeId);
        stats.setTotalVotes(totalVotes);
        stats.setLikeCount(likeCount);
        stats.setDislikeCount(dislikeCount);
        stats.setUserVote(userVote);
        stats.calculateRatios(); // 비율 계산

        return stats;
    }

    /**
     * 사용자의 특정 미로 투표 상태 조회
     * @param mazeId 미로 ID
     * @param username 사용자명
     * @return 투표 상태 (null: 투표안함, true: 좋아요, false: 싫어요)
     */
    @Transactional(readOnly = true)
    public Boolean getUserVoteStatus(Long mazeId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다: " + username));

        Optional<MazeVote> vote = mazeVoteRepository.findByMazeIdAndUserId(mazeId, user.getId());
        return vote.map(MazeVote::getIsLike).orElse(null);
    }

    /**
     * 미로 삭제 시 관련 투표들도 삭제
     * @param mazeId 미로 ID
     */
    @Transactional
    public void deleteVotesByMazeId(Long mazeId) {
        mazeVoteRepository.deleteByMazeId(mazeId);
    }

    /**
     * 투표 취소
     * @param mazeId 미로 ID
     * @param username 사용자명
     */
    @Transactional
    public MazeVoteStatsDto cancelVote(Long mazeId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다: " + username));

        Optional<MazeVote> vote = mazeVoteRepository.findByMazeIdAndUserId(mazeId, user.getId());
        if (vote.isPresent()) {
            mazeVoteRepository.delete(vote.get());
        }

        return getMazeVoteStats(mazeId, username);
    }
}