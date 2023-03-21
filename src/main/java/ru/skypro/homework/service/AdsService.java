package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.Ads;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exception.AdsNotFoundException;
import ru.skypro.homework.exception.RequestDeniedException;
import ru.skypro.homework.mapper.AdsMapper;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.UserRepository;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for work with ads
 */
@Service
public class AdsService {
    private final AdsRepository adsRepository;
    private final CommentService commentService;
    private final AdsMapper adsMapper;
    private final UserMapper userMapper;
    private final ImageService imageService;
    private final UserService userService;
    private final UserRepository userRepository;

    /**
     * @param adsRepository
     * @param commentService
     * @param adsMapper
     * @param userMapper
     * @param imageService
     * @param userService
     * @param userRepository
     */
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

    /**
     * Method for return all ads
     *
     * @return ResponseWrapperAdsDto
     */
    public ResponseWrapperAdsDto getAllAds() {
        List<Ads> listAds = adsRepository.findAll();
        List<AdsDto> listAdsDto = new ArrayList<>();
        for (Ads listAd : listAds) {
            listAdsDto.add(adsMapper.adsToDTO(listAd));
        }
        return new ResponseWrapperAdsDto(listAdsDto.size(), listAdsDto);
    }

    /**
     * Method for create ads
     *
     * @param userName
     * @param createAdsDto
     * @param multipartFile
     * @return adsMapper.adsToDTO
     */
    public AdsDto addAds(String userName, CreateAdsDto createAdsDto, MultipartFile multipartFile) {
        Ads ads = adsMapper.adsToEntity(createAdsDto);
        ads.setUser(userMapper.toEntity(userService.getUserDto(userName)));
        ads.setImage(imageService.addImage(ads, multipartFile));
        userService.updateUser(userName, userService.getUserDto(userName));
        ads = adsRepository.save(ads);
        return adsMapper.adsToDTO(ads);
    }

    /**
     * Method for return all comments by ads id
     *
     * @param adPk
     * @return commentService.getAllCommentsByAds
     */
    public ResponseWrapperCommentDto getAllCommentsByAdsId(int adPk) {
        Ads ads = adsRepository.findById(adPk).orElseThrow(AdsNotFoundException::new);
        return commentService.getAllCommentsByAds(ads);
    }

    /**
     * Method for add comment by ads
     *
     * @param username
     * @param commentDto
     * @param adPk
     * @return commentService.addComments
     */
    public CommentDto addComments(String username, int adPk, CommentDto commentDto) {
        if (adPk < 0 || commentDto == null) {
            throw new IllegalArgumentException();
        }
        Ads ads = adsRepository.findById(adPk).orElseThrow(AdsNotFoundException::new);
        commentDto.setPk(adPk);
        return commentService.addComments(username, ads, commentDto);
    }

    /**
     * Method for return full ads dto by ads id
     *
     * @param id
     * @return adsMapper.adsToFullAdsDTO
     */
    public FullAdsDto getFullAds(int id) {
        if (id < 0) {
            throw new IllegalArgumentException();
        }
        Ads ads = adsRepository.findById(id).orElseThrow(AdsNotFoundException::new);

        return adsMapper.adsToFullAdsDTO(ads);
    }

    /**
     * Method for delete ads
     *
     * @param username
     * @param id
     * @return adsMapper.adsToFullAdsDTO
     */
    public FullAdsDto deleteAds(String username, int id) {
        User user = userRepository.findByUserName(username);
        Ads ads = adsRepository.findById(id).orElseThrow(AdsNotFoundException::new);
        if (!user.getRole().equals(Role.ADMIN) && !ads.getUser().equals(user)) {
                throw new RequestDeniedException();
            }
        commentService.deleteALLCommentOfAds(ads);
        imageService.deleteImageFile(Path.of(ads.getImage().getPathImage()));
        adsRepository.deleteById(id);
        return adsMapper.adsToFullAdsDTO(ads);
    }

    /**
     * Method for update ads
     *
     * @param username
     * @param id
     * @param createAdsDto
     * @return adsMapper.adsToDTO
     */
    public AdsDto updateAds(String username, int id, CreateAdsDto createAdsDto) {
        User user = userRepository.findByUserName(username);
        Ads ads = adsRepository.findById(id).orElseThrow(AdsNotFoundException::new);
        if (!user.getRole().equals(Role.ADMIN) && !ads.getUser().equals(user)) {
            throw new RequestDeniedException();
        }
        ads.setDescription(createAdsDto.getDescription());
        ads.setPrice(createAdsDto.getPrice());
        ads.setTitle(createAdsDto.getTitle());
        adsRepository.save(ads);
        return adsMapper.adsToDTO(ads);
    }

    /**
     * Method for get comment by ads and comment id
     *
     * @param adPk
     * @param id
     * @return commentService.getCommentOfAds
     */
    public CommentDto getCommentOfAds(int adPk, int id) {
        adsRepository.findById(adPk).orElseThrow(AdsNotFoundException::new);
        return commentService.getCommentOfAds(id);
    }

    /**
     * Method for delete comment by ads
     *
     * @param username
     * @param adPk
     * @param id
     */
    public void deleteCommentOfAds(String username, int adPk, int id) {
        User user = userRepository.findByUserName(username);
        adsRepository.findById(adPk).orElseThrow(AdsNotFoundException::new);
        Comment comment = commentService.getCommentById(id);
        System.out.println(user.getRole().equals(Role.ADMIN)&&!comment.getUser().equals(user));
        if (!user.getRole().equals(Role.ADMIN)&&!comment.getUser().equals(user)) {
            throw new RequestDeniedException();}
        commentService.deleteCommentOfAds(id);
    }

    /**
     * Method for update comment
     *
     * @param username
     * @param adPk
     * @param id
     * @param commentDto
     * @return commentService.updateComments
     */
    public CommentDto updateComments(String username, int adPk, int id, CommentDto commentDto) {
        User user = userRepository.findByUserName(username);
        Ads ads = adsRepository.findById(adPk).orElseThrow(AdsNotFoundException::new);
        if (!ads.getUser().equals(user)) {
            throw new RequestDeniedException();
        }
        return commentService.updateComments(ads, id, commentDto);
    }

    /**
     * Method for get all ads by username
     *
     * @param userName
     * @return ResponseWrapperAdsDto
     */
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
