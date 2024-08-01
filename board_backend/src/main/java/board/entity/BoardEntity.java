package board.entity;


import java.time.LocalDateTime;
import java.util.Collection;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_jpa_board")
@NoArgsConstructor
@Data
public class BoardEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int boardIdx;
	
	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false)
	private String contents;
	
	@Column(nullable = false)
	private int hitCnt = 0;
	
	@Column(nullable = false)
	private LocalDateTime createdDatetime = LocalDateTime.now();
	
	@Column(nullable = false)
	private String creatorId;
	
	private LocalDateTime updatedDatetime;
	
	private String updatorId;
	
	// @OneToMany : 1:N 관계를 표현하는 JAP 애노테이션
	// FetchType.EAGER : 즉시 로딩. 부모 엔티티(BoardEntity)와 자식 엔티티(BoarFileEntity)를 한번에 조회 
	// FetchType.LAZY : 지연 로딩. 부모 엔티티를 조회할 때 자식 엔티티를 조회하지 않음 
	// CascadeType : 영속성 전이 (부모 엔티티의 변화를 자식 엔티티로 전이하는 방식)
	// @JoinColumn : 릴레이션 관계가 있는 테이블의 컬럼을 정의 
	/*
 	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "board_idx")
	*/
	// BoardFileEntity 클래스의 board 필드가 이 관계의 주인임을 지정
@OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Collection<BoardFileEntity> fileInfoList;
}
