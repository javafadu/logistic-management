package com.logistic.service;

import com.logistic.domain.Role;
import com.logistic.domain.User;
import com.logistic.domain.enums.RoleType;
import com.logistic.dto.UserDTO;
import com.logistic.dto.mapper.UserMapper;
import com.logistic.dto.request.*;
import com.logistic.dto.response.LoginResponse;
import com.logistic.dto.response.UserResponse;
import com.logistic.exception.BadRequestException;
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
import org.springframework.transaction.annotation.Transactional;

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

    // Get User by Email
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


    // Login Service
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


    // Password Update
    public void updatePassword(UpdatePasswordRequest updatePasswordRequest) {

        User user = getCurrentLoggedInUser();

        // check1 : check if there is any builtIn situation
        // check2 : old password is the same in the stored password in db
        if(!passwordEncoder.matches(updatePasswordRequest.getOldPassword(),user.getPassword())) {
            throw new BadRequestException(ErrorMessages.PASSWORD_NOT_MATCHED_MESSAGE);
        }
        // check3: encode the new password
        String encodedNewPassword = passwordEncoder.encode(updatePasswordRequest.getNewPassword());

        user.setPassword(encodedNewPassword);

        userRepository.save(user);

    }

    // User Update by authenticated user
    @Transactional // for update and delete operation, all queries roll back in case of any exception issue
    public void updateUser(UserUpdateRequest userUpdateRequest) {
        User user = getCurrentLoggedInUser();

        // check e-mail if exist or belongs to anyone
       Boolean emailExist =  userRepository.existsByEmail(userUpdateRequest.getEmail());

       if(emailExist && !userUpdateRequest.getEmail().equals(user.getEmail())) {
           throw new ConflictException(String.format(ErrorMessages.EMAIL_ALREADY_EXIST_ERROR_MESSAGE,userUpdateRequest.getEmail()));
       }

       userRepository.update(user.getId(),
               userUpdateRequest.getName(),
               userUpdateRequest.getEmail(),
               userUpdateRequest.getPhone(),
               userUpdateRequest.getBirthDate() );

    }

    // User update by admin
    public void adminUserUpdate(Long id, AdminUserUpdateRequest adminUserUpdateRequest) {
        // check1: if user exist or not
       User user = getById(id);

       // check2: e-mail control
        Boolean emailExist =  userRepository.existsByEmail(adminUserUpdateRequest.getEmail());

        if(emailExist && !adminUserUpdateRequest.getEmail().equals(user.getEmail())) {
            throw new ConflictException(String.format(ErrorMessages.EMAIL_ALREADY_EXIST_ERROR_MESSAGE,adminUserUpdateRequest.getEmail()));
        }
        // check3: password is null or not
        if(adminUserUpdateRequest.getPassword()==null) {
            adminUserUpdateRequest.setPassword(user.getPassword());
        } else {
            String encodedPassword= passwordEncoder.encode(adminUserUpdateRequest.getPassword());
            adminUserUpdateRequest.setPassword(encodedPassword);
        }
        // check4: roles
        Set<String> userStrRoles = adminUserUpdateRequest.getRoles();
        Set<Role> convertedRoles = convertRoles(userStrRoles);

        user.setName(adminUserUpdateRequest.getName());
        user.setEmail(adminUserUpdateRequest.getEmail());
        user.setPhone(adminUserUpdateRequest.getPhone());
        user.setBirthDate(adminUserUpdateRequest.getBirthDate());
        user.setPassword(adminUserUpdateRequest.getPassword());
        user.setRoles(convertedRoles);

        userRepository.save(user);

    }

    private Set<Role> convertRoles(Set<String> pRoles) { // pRoles={"User", "Administrator"}
        Set<Role> roles = new HashSet<>();

        if(pRoles==null) {
            Role userRole = roleService.findByType(RoleType.ROLE_USER);
            roles.add(userRole);
        } else {
            pRoles.forEach(roleStr-> {
                if(roleStr.equals(RoleType.ROLE_USER.getName())) {
                    Role userRole = roleService.findByType(RoleType.ROLE_USER);
                    roles.add(userRole);
                } else if(roleStr.equals(RoleType.ROLE_ADMIN.getName())) {
                    Role adminRole = roleService.findByType(RoleType.ROLE_ADMIN);
                    roles.add(adminRole);
                } else if(roleStr.equals(RoleType.ROLE_CUSTOMER.getName())) {
                    Role customerRole = roleService.findByType(RoleType.ROLE_CUSTOMER);
                    roles.add(customerRole);
                } else if(roleStr.equals(RoleType.ROLE_SUPPLIER.getName())) {
                    Role supplierRole = roleService.findByType(RoleType.ROLE_SUPPLIER);
                    roles.add(supplierRole);
                } else if (roleStr.equals(RoleType.ROLE_MANAGER.getName())) {
                    Role managerROle = roleService.findByType(RoleType.ROLE_MANAGER);
                    roles.add(managerROle);
                }
            });
        }
            return roles;
    }

    public User getById(Long id) {
        User user = userRepository.findUserById(id).orElseThrow(()->
                 new ResourceNotFoundException(String.format(ErrorMessages.USER_WITH_ID_NOT_FOUND_EXCEPTION,id)));

        return user;

    }

    // Delete a user
    public void deleteAccountById(Long id) {
        User user = getById(id);

        // check user has address or order or others if any transaction related to user then delete it
        userRepository.deleteById(id);
    }
}
