package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.ResponseWrapperCommentDto;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for work with comment
 */
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;

    /**
     * @param commentRepository
     * @param commentMapper
     * @param userRepository
     */
    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.userRepository = userRepository;
    }

    /**
     * Method for return all comment by ads
     *
     * @param ads
     * @return ResponseWrapperCommentDto
     */
    public ResponseWrapperCommentDto getAllCommentsByAds(Ads ads) {
        List<CommentDto> dtoList = new ArrayList<>();
        List<Comment> listComments = commentRepository.findAllByAds(ads);

        for (int i = 0; i < listComments.size(); i++) {
            dtoList.add(i, commentMapper.commentToDTO(listComments.get(i)));
        }
        return new ResponseWrapperCommentDto(dtoList.size(), dtoList);
    }

    /**
     * Method for create comments
     *
     * @param username
     * @param ads
     * @param commentDto
     * @return commentDto
     */
    public CommentDto addComments(String username, Ads ads, CommentDto commentDto) {
        commentDto.setCreatedAt(LocalDateTime.now().toString());
        commentDto.setAuthor(userRepository.findByUserName(username).getId());
        Comment comment = commentMapper.commentDtoToEntity(commentDto);
        comment.setAds(ads);
        comment.setUser(userRepository.findByUserName(username));
        commentRepository.save(comment);
        return commentDto;
    }

    /**
     * Method for return comment of ads
     *
     * @param id
     * @return commentMapper.commentToDTO
     */
    public CommentDto getCommentOfAds(int id) {
        Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        checkComment(comment);
        return commentMapper.commentToDTO(comment);
    }

    /**
     * Method for delete comment of ads
     *
     * @param id
     */
    public void deleteCommentOfAds( int id) {
        commentRepository.delete(commentRepository.findById(id).orElseThrow(CommentNotFoundException::new));
    }

    /**
     * Method for update comments
     *
     * @param ads
     * @param id
     * @param commentDto
     * @return commentMapper.commentToDTO
     */
    public CommentDto updateComments(Ads ads, int id, CommentDto commentDto) {
        Comment comment = commentRepository.findCommentByAdsAndId(ads, id);
        checkComment(comment);
        comment.setText(commentDto.getText());
        commentRepository.save(comment);
        return commentMapper.commentToDTO(comment);
    }

    /**
     * Method for check comment
     *
     * @param comment
     */
    private void checkComment(Comment comment) {
        if (comment == null) {
            throw new CommentNotFoundException();
        }
    }

    /**
     * Method for delete all comment of ads
     *
     * @param ads
     */
    public void deleteALLCommentOfAds(Ads ads) {
        List<Comment> allCommentsOfAds = commentRepository.findAllByAds(ads);
        for (Comment allCommentsOfAd : allCommentsOfAds) {
            commentRepository.delete(commentRepository.findById(allCommentsOfAd.getId()).orElseThrow(CommentNotFoundException::new));
        }
    }

    /**
     * Method for get comment by  id
     *
     * @param id
     * @return commentRepository.findById
     */
    public Comment getCommentById(Integer id) {
        return commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
    }
}
