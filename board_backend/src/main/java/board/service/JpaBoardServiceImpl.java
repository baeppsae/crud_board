package board.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import board.common.FileUtils;
import board.entity.BoardEntity;
import board.entity.BoardFileEntity;
import board.repository.JpaBoardRepository;

@Service
public class JpaBoardServiceImpl implements JpaBoardService {

	@Autowired
	private JpaBoardRepository jpaBoardRepository;
	
	@Autowired
	private FileUtils fileUtils;
	
	@Override
	public List<BoardEntity> selectBoardList() {
		return jpaBoardRepository.findAllByOrderByBoardIdxDesc();
	}

	@Override
	public void insertBoard(BoardEntity boardEntity, MultipartHttpServletRequest request) throws Exception {
		// FileUtils 클래스에 paraseFileInfo(request) 메서드를 추가
		// @OneToMany 연관관계를 설정했기 때문에 첨부파일에 게시판 번호를 따로 지정하지 않아도 자동으로 설정
		List<BoardFileEntity> list = fileUtils.parseFileInfo(request);
		if (list != null) {
			for (BoardFileEntity file : list) {
				file.setBoard(boardEntity);
			}
			boardEntity.setFileInfoList(list);
		}
		// TODO. 로그인한 사용자 계정으로 변경
		boardEntity.setCreatorId("tester");
				
		// 리포지터의 save 메서드는 insert와 update 두 가지 역할을 수행 
		jpaBoardRepository.save(boardEntity);
	}

	@Override
	public void updateBoard(BoardEntity boardEntity) throws Exception {
		// TODO. 로그인한 사용자 계정으로 변경
		boardEntity.setCreatorId("tester");
				
		// 리포지터의 save 메서드는 insert와 update 두 가지 역할을 수행 
		jpaBoardRepository.save(boardEntity);
	}
	
	@Override
	public void deleteBoard(int boardIdx) {
		jpaBoardRepository.deleteById(boardIdx);
	}

	@Override
	public BoardFileEntity selectBoardFileInfo(int idx, int boardIdx) {
		return jpaBoardRepository.findBoardFile(idx, boardIdx);
	}

	@Override
	public BoardEntity selectBoardDetail(int boardIdx) throws Exception {
		Optional<BoardEntity> optional = jpaBoardRepository.findById(boardIdx);
		if (optional.isPresent()) {
			BoardEntity boardEntity = optional.get();
			boardEntity.setHitCnt(boardEntity.getHitCnt() + 1);
			jpaBoardRepository.save(boardEntity);
			return boardEntity;
		} else {
			throw new Exception("일치하는 데이터가 없음");
		}
	}

}
