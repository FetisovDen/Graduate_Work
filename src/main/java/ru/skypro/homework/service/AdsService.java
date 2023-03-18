package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Avatar;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.AdsNotFoundException;
import ru.skypro.homework.exception.RequestDeniedException;
import ru.skypro.homework.mapper.AdsMapper;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.UserRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AdsService {
    private final AdsRepository adsRepository;
    private final CommentService commentService;
    private final AdsMapper adsMapper;
    private final UserMapper userMapper;
    private final ImageService imageService;
    private final UserService userService;
    private final UserRepository userRepository;

    public AdsService(AdsRepository adsRepository, CommentService commentService, AdsMapper adsMapper,
                      UserMapper userMapper, ImageService imageService, UserService userService,
                      UserRepository userRepository) {
        this.adsRepository = adsRepository;
        this.commentService = commentService;
        this.adsMapper = adsMapper;
        this.userMapper = userMapper;
        this.imageService = imageService;
        this.userService = userService;
        this.userRepository = userRepository;
    }


    public ResponseWrapperAdsDto getAllAds() {
        List<Ads> listAds = adsRepository.findAll();
        List<AdsDto> listAdsDto = new ArrayList<>();
        for (Ads listAd : listAds) {
            listAdsDto.add(adsMapper.adsToDTO(listAd));
        }
        return new ResponseWrapperAdsDto(listAdsDto.size(), listAdsDto);
    }

    public AdsDto addAds(String userName, CreateAdsDto createAdsDto, MultipartFile multipartFile) {
        Ads ads = adsMapper.adsToEntity(createAdsDto);
        User user = userMapper.toEntity(userService.getUserDto(userName));
        ads.setUser(user);
        ads.setImage(imageService.addImage(ads, multipartFile));
        userService.updateUser(userName, userMapper.toDTO(ads.getUser()));
        ads = adsRepository.save(ads);
        return adsMapper.adsToDTO(ads);
    }

    public ResponseWrapperCommentDto getAllCommentsByAdsId(int adPk) {
        Ads ads = adsRepository.findById(adPk).orElseThrow(AdsNotFoundException::new);
        return commentService.getAllCommentsByAds(ads);
    }

    public CommentDto addComments(String username,int adPk, CommentDto commentDto) {
        if (adPk < 0 || commentDto == null) {
            throw new IllegalArgumentException();
        }
        Ads ads = adsRepository.findById(adPk).orElseThrow(AdsNotFoundException::new);
        commentDto.setPk(adPk);
        return commentService.addComments(username,ads, commentDto);
    }


    public FullAdsDto getFullAds(int id) {
        if (id < 0) {
            throw new IllegalArgumentException();
        }
        Ads ads = adsRepository.findById(id).orElseThrow(AdsNotFoundException::new);

        return adsMapper.adsToFullAdsDTO(ads);
    }

    public FullAdsDto deleteAds(String username, int id) {
        User user = userRepository.findByUserName(username);
        Ads ads = adsRepository.findById(id).orElseThrow(AdsNotFoundException::new);
        if(!ads.getUser().equals(user)) {throw new RequestDeniedException();}
        adsRepository.deleteById(id);
        return adsMapper.adsToFullAdsDTO(ads);
    }

    public AdsDto updateAds(String username, int id, CreateAdsDto createAdsDto) {
        User user = userRepository.findByUserName(username);
        Ads ads = adsRepository.findById(id).orElseThrow(AdsNotFoundException::new);
        if(!ads.getUser().equals(user)) {throw new RequestDeniedException();}
        ads.setDescription(createAdsDto.getDescription());
        ads.setPrice(createAdsDto.getPrice());
        ads.setTitle(createAdsDto.getTitle());
        adsRepository.save(ads);
        return adsMapper.adsToDTO(ads);
    }

    public CommentDto getCommentOfAds(int adPk, int id) {
        Ads ads = adsRepository.findById(adPk).orElseThrow(AdsNotFoundException::new);
        return commentService.getCommentOfAds(ads, id);
    }

    public void deleteCommentOfAds(String username, int adPk, int id) {
        User user = userRepository.findByUserName(username);
        Ads ads = adsRepository.findById(adPk).orElseThrow(AdsNotFoundException::new);
        if(!ads.getUser().equals(user)) {throw new RequestDeniedException();}
        commentService.deleteCommentOfAds(id);
    }

    public CommentDto updateComments(String username, int adPk, int id, CommentDto commentDto) {
        User user = userRepository.findByUserName(username);
        Ads ads = adsRepository.findById(adPk).orElseThrow(AdsNotFoundException::new);
        if(!ads.getUser().equals(user)) {throw new RequestDeniedException();}
        return commentService.updateComments(ads, id, commentDto);
    }

    public ResponseWrapperAdsDto getAdsMe(String userName) {
        User user = userMapper.toEntity(userService.getUserDto(userName));
        List<Ads> listAds = adsRepository.findAllByUser(user);
        List<AdsDto> listAdsDto = new ArrayList<>();
        for (Ads listAd : listAds) {
            listAdsDto.add(adsMapper.adsToDTO(listAd));
        }
        return new ResponseWrapperAdsDto(listAdsDto.size(), listAdsDto);
    }
}
