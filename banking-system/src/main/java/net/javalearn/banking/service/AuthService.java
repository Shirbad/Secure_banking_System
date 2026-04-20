package net.javalearn.banking.service;

import net.javalearn.banking.dto.AuthRequest;
import net.javalearn.banking.dto.AuthResponse;
import net.javalearn.banking.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(AuthRequest request);
}
