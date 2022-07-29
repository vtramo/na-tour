package com.natour.natour.services.authentication.jwt;

import java.util.List;
import java.util.Map;

import com.natour.natour.model.Token;

public interface TokenGeneratorService {
    Token generateToken(Map<String, List<String>> params, TokenScope tokenScope);
}
