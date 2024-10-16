package com.petrovdns.vaadin.data.service;

import com.petrovdns.vaadin.data.dto.UserDTO;
import com.petrovdns.vaadin.data.entity.BlogPost;
import com.petrovdns.vaadin.data.entity.Company;
import com.petrovdns.vaadin.data.entity.Status;
import com.petrovdns.vaadin.data.entity.UserEntity;
import com.petrovdns.vaadin.data.mapper.CycleAvoidingMappingContext;
import com.petrovdns.vaadin.data.mapper.UserMapper;
import com.petrovdns.vaadin.data.repository.BlogPostRepository;
import com.petrovdns.vaadin.data.repository.CompanyRepository;
import com.petrovdns.vaadin.data.repository.ContactRepository;
import com.petrovdns.vaadin.data.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <p>Instagram: @petrovdns
 * <p>Telegram: +37379666011 | @ixyck
 */

@Service
public class BlogService {
    private BlogPost currentBlogPost;


    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private BlogPostRepository blogPostRepository;

//    @Autowired
//    PasswordEncoder passwordEncoder;

    public List<UserDTO> findAllContacts(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            return UserMapper.INSTANCE.mapToUserDto(contactRepository.findAll(), new CycleAvoidingMappingContext());
        } else {
            return UserMapper.INSTANCE.mapToUserDto(contactRepository.search(filterText), new CycleAvoidingMappingContext());
        }
    }

    public UserEntity findUserByUserName(String username) {
        return contactRepository.findByUsername(username);
    }

    public List<BlogPost> findAllBlogPost(String blogPost) {
        if (blogPost == null || blogPost.isEmpty()) {
            return blogPostRepository.findAll();
        } else {
            return blogPostRepository.searchBlogByTitle(blogPost);
        }
    }

    public BlogPost findBlogPostById(long blogPostId) {
        BlogPost post = null;
        Optional<BlogPost> blogPost = blogPostRepository.findById(blogPostId);
        if (blogPost.isPresent()) {
            post = blogPost.get();
        }
        return post;
    }

    public List<BlogPost> findBlogPostByAuthor(Long id) {
        return blogPostRepository.searchBlogPostByAuthor(id);
    }

    public void saveBlogPost(BlogPost blogPost) {
        if (blogPost == null) {
            System.err.println("Blog post is null");
            return;
        }
        blogPostRepository.save(blogPost);
    }

    public void deleteBlogPost(BlogPost blogPost) {
        blogPostRepository.delete(blogPost);
    }

    public long countContacts() {
        return contactRepository.count();
    }

    public void deleteContact(UserDTO userDTO) {
        contactRepository.delete(UserMapper.INSTANCE.mapToUser(userDTO, new CycleAvoidingMappingContext()));
    }

    public void saveContact(UserDTO userDTO) {
        if (userDTO == null) {
            System.err.println("UserDTO is null");
            return;
        }

//        if() {
//            String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
//            userDTO.setPassword(encodedPassword);
//        }
        contactRepository.save(UserMapper.INSTANCE.mapToUser(userDTO, new CycleAvoidingMappingContext()));
    }

    public void registerUser(UserEntity userEntity) {
        if (userEntity == null) {
            System.err.println("UserDTO is null");
            return;
        }
        contactRepository.save(userEntity);
    }

    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }

    public List<Status> findAllStatuses() {
        return statusRepository.findAll();
    }

    public void setCurrentBlogPost(BlogPost currentBlogPost) {
        this.currentBlogPost = currentBlogPost;
    }

    public BlogPost getCurrentBlogPost() {
        return currentBlogPost;
    }

    public void removeCurrentBlogPost() {
        currentBlogPost = null;
    }
}
