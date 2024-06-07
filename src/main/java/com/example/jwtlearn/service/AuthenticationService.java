package com.example.jwtlearn.service;

import com.example.jwtlearn.models.User;
import com.example.jwtlearn.repositories.UserRepository;
import com.example.jwtlearn.response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationResponse register(User request){
       User user=new User();
       user.setFirstName(request.getFirstName());
       user.setLastName(request.getLastName());
       user.setUsername(request.getUsername());
       user.setPassword(passwordEncoder.encode(request.getPassword()));
       user.setRole(request.getRole());
       user=userRepository.save(user);
       String token =jwtService.generateToken(user);
       return new AuthenticationResponse(token);

    }

    public AuthenticationResponse authenticate(User request){
      authenticationManager.authenticate(
             new UsernamePasswordAuthenticationToken(
                      request.getUsername(),
                      request.getPassword()
              )
      );

      User user=userRepository.findByUsername(request.getUsername()).orElseThrow();
      String token=jwtService.generateToken(user);
      return new AuthenticationResponse(token);

    }


}
