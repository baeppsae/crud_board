package board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import board.entity.BoardEntity;
import board.entity.BoardFileEntity;

public interface JpaBoardRepository extends JpaRepository<BoardEntity, Integer> {
	// boardIdx를 내림차순으로 정렬한 데이트를 조회
	List<BoardEntity> findAllByOrderByBoardIdxDesc();
	
	@Query("SELECT file FROM BoardFileEntity file WHERE file.idx = :idx AND file.board.boardIdx = :boardIdx")
	public BoardFileEntity findBoardFile(@Param("idx") int idx, @Param("boardIdx") int boardIdx);
}
