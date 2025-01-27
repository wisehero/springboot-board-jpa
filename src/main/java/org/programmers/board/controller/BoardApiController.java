package org.programmers.board.controller;

import org.programmers.board.dto.BoardCreateRequest;
import org.programmers.board.dto.BoardResponse;
import org.programmers.board.dto.BoardUpdateRequest;
import org.programmers.board.dto.UserDTO;
import org.programmers.board.entity.Board;
import org.programmers.board.entity.vo.Content;
import org.programmers.board.entity.vo.Title;
import org.programmers.board.service.BoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class BoardApiController {

    private static final Logger log = LoggerFactory.getLogger(BoardApiController.class);
    private final BoardService boardService;

    public BoardApiController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 페이징 조회
    @GetMapping
    public ResponseEntity<Page<BoardResponse>> getPages(Pageable pageable) {
        Page<BoardResponse> boards = boardService.getBoards(pageable).map(this::getBoardResponse);

        return ResponseEntity.ok(boards);
    }

    // 단건 조회
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardResponse> getBoard(@PathVariable Long id) {
        Board board = boardService.getBoard(id);

        BoardResponse boardResponse = getBoardResponse(board);

        return ResponseEntity.ok(boardResponse);
    }

    private BoardResponse getBoardResponse(Board board) {
        return new BoardResponse(board.getId(), board.getTitle().getTitle(), board.getContent().getContent(), board.getUser().getName().getName());
    }

    // POST : 게시글 작성
    @PostMapping
    public ResponseEntity<BoardResponse> create(@RequestBody BoardCreateRequest boardCreateRequest) {
        Board newBoard = new Board(new Title(boardCreateRequest.getTitle()), new Content(boardCreateRequest.getContent()), UserDTO.toEntity(boardCreateRequest.getUserDTO()));

        Long createdBoardId = boardService.createBoard(newBoard);
        Board board = boardService.getBoard(createdBoardId);

        BoardResponse boardResponse = getBoardResponse(board);

        return ResponseEntity.status(HttpStatus.CREATED).body(boardResponse);
    }

    // POST : 게시글 수정
    @PatchMapping("/{id}")
    public ResponseEntity<BoardResponse> updateBoard(@RequestBody BoardUpdateRequest boardUpdateRequest, @PathVariable Long id) {

        Long updatedBoardId = boardService.updateBoard(id, boardUpdateRequest);
        Board updatedBoard = boardService.getBoard(updatedBoardId);

        BoardResponse boardResponse = getBoardResponse(updatedBoard);

        return ResponseEntity.ok(boardResponse);
    }
}