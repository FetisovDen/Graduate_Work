package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.ResponseWrapperCommentDto;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.exception.AdsNotFoundException;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final AdsRepository adsRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, AdsRepository adsRepository, CommentMapper commentMapper, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.adsRepository = adsRepository;
        this.commentMapper = commentMapper;
        this.userRepository = userRepository;
    }

    public ResponseWrapperCommentDto getAllCommentsByAds(Ads ads) {
        List<CommentDto> dtoList = new ArrayList<>();
        List<Comment> listComments = commentRepository.findAllByAds(ads);

        for (int i = 0; i < listComments.size(); i++) {
            dtoList.add(i, commentMapper.commentToDTO(listComments.get(i)));
        }
        return new ResponseWrapperCommentDto(dtoList.size(), dtoList);
    }

    public CommentDto addComments(String username, Ads ads, CommentDto commentDto) {
        commentDto.setCreatedAt(LocalDateTime.now().toString());
        commentDto.setAuthor(userRepository.findByUserName(username).getId());
        Comment comment = commentMapper.commentDtoToEntity(commentDto);
        comment.setAds(ads);
        comment.setUser(userRepository.findByUserName(username));
        commentRepository.save(comment);
        return commentDto;
    }

    public CommentDto getCommentOfAds(Ads ads, int id) {
        Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        checkComment(comment);
        return commentMapper.commentToDTO(comment);
    }

    public void deleteCommentOfAds( int id) {
        commentRepository.delete(commentRepository.findById(id).orElseThrow(CommentNotFoundException::new));
    }

    public CommentDto updateComments(Ads ads, int id, CommentDto commentDto) {
        Comment comment = commentRepository.findCommentByAdsAndId(ads, id);
        checkComment(comment);
        comment.setText(commentDto.getText());
        commentRepository.save(comment);
        return commentMapper.commentToDTO(comment);
    }

    private void checkComment(Comment comment) {
        if (comment == null) {
            throw new CommentNotFoundException();
        }
    }
}
