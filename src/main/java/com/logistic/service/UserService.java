package com.logistic.service;

import com.logistic.domain.Role;
import com.logistic.domain.User;
import com.logistic.domain.enums.RoleType;
import com.logistic.dto.UserDTO;
import com.logistic.dto.mapper.UserMapper;
import com.logistic.dto.request.LoginRequest;
import com.logistic.dto.request.UserRegisterRequest;
import com.logistic.dto.response.LoginResponse;
import com.logistic.dto.response.UserResponse;
import com.logistic.exception.ConflictException;
import com.logistic.exception.ResourceNotFoundException;
import com.logistic.exception.messages.ErrorMessages;
import com.logistic.repository.UserRepository;
import com.logistic.security.SecurityUtils;
import com.logistic.security.jwt.JwtUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;


    public UserService(UserRepository userRepository, RoleService roleService, @Lazy PasswordEncoder passwordEncoder, UserMapper userMapper, @Lazy AuthenticationManager authManager, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.authManager=authManager;
        this.jwtUtils=jwtUtils;
    }

    public User getUserByEmail(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND_EXCEPTION, email)));

        return user;
    }


    // Create an Admin account with startup
    public UserResponse addAdminWithStart() {
        if (userRepository.findAdminUsers(5).size() == 0) {

            Set<Role> roles = new HashSet<>();
            Role role1 = new Role();
            role1.setType(RoleType.ROLE_USER);
            role1.setId(1);
            Role role2 = new Role();
            role2.setType(RoleType.ROLE_ADMIN);
            role2.setId(5);

            LocalDateTime today = LocalDateTime.now();

            roles.add(role1);
            roles.add(role2);

            User user = new User();
            user.setName("Admin User");
            user.setEmail("admin@mail.com");
            user.setPhone("444-3332222");
            user.setStatus("active");
            user.setRegisterDate(today);
            user.setPassword("$2a$10$lr4YO0EpFWwhkgQeoaUTTe2FRCHC9q1LltLG5IMY.HslqyNcbPf.m"); // Tren3434.?
            user.setRoles(roles);

            return userMapper.userToUserResponse(userRepository.save(user));
        }
        return null;
    }



    // Register a user by public
    public void saveUser(UserRegisterRequest registerRequest) {
        // check1: control e-mail is exist or not
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ConflictException(String.format(ErrorMessages.EMAIL_ALREADY_EXIST_ERROR_MESSAGE, registerRequest.getEmail()));
        }

        // Check2: set the role as User
        Role role = roleService.findByType(RoleType.ROLE_USER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        // Check3: encode the string password before saving in the DB
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

        // SET all related fields and save
        LocalDateTime today = LocalDateTime.now();
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encodedPassword);
        user.setBirthDate(registerRequest.getBirthDate());
        user.setStatus("active");
        user.setPhone(registerRequest.getPhone());
        user.setRegisterDate(today);
        user.setRoles(roles);

        userRepository.save(user);
    }


    public LoginResponse authenticate(LoginRequest loginRequest) {
        // STEP1 : get username and password and authenticate
        // (convert email and password --> username and password in spring securitt)
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        Authentication authentication = authManager.authenticate(usernamePasswordAuthenticationToken);
        // user has been authenticated (validated) and ready to generate token

        // STEP2 : start a Jwt Token
        // get information of currently logged in user
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // generate a Jwt Token
        String token = jwtUtils.generateJwtToken(userDetails);
        LoginResponse response = new LoginResponse();
        response.setToken(token);

        return response;
    }

    // Get All Users
    public List<UserDTO> getAllUsers() {

        List<User> users = userRepository.findAll();
        return userMapper.userListToUserDTOList(users);

    }

    // Get Principal
    public UserDTO getPrincipal() {

        User user = getCurrentLoggedInUser();
        return userMapper.userToUserDTO(user);

    }


    // Get Currently Logged in User (fix method)
    public User getCurrentLoggedInUser() {
        String email = SecurityUtils.getCurrentLoggedInUser().orElseThrow(()->
                new ResourceNotFoundException(ErrorMessages.PRINCIPAL_NOT_FOUND_MESSAGE));

        User user = getUserByEmail(email);
        return user;
    }


    public Page<UserDTO> getAllUsersWithPaging(String q, Pageable pageable) {
        Page<User> allUsersWithPaging = null;
        if (!q.isEmpty()) {
            allUsersWithPaging = userRepository.getSearchedUsersWithPaging(q.toLowerCase(), pageable);
        } else {
            allUsersWithPaging = userRepository.findAll(pageable);
        }
        return convertPageUserToPageUserDTO(allUsersWithPaging);
    }

    // convert Page<User> to Page<UserDTO> fixed method
    private Page<UserDTO> convertPageUserToPageUserDTO(Page<User> users) {
        return users.map(
                user -> userMapper.userToUserDTO(user)
        );
    }


    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow( ()->
                new ResourceNotFoundException(String.format(ErrorMessages.USER_WITH_ID_NOT_FOUND_EXCEPTION,id))
        );


       return userMapper.userToUserDTO(user);

    }
}
