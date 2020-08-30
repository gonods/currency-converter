package com.example.converter.service;

import com.example.converter.model.History;
import com.example.converter.model.HistoryDto;
import com.example.converter.model.User;
import com.example.converter.repository.HistoryRepository;
import com.example.converter.repository.UserRepository;
import com.example.converter.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoryService {

    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public HistoryService(UserRepository userRepository, HistoryRepository historyRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.historyRepository = historyRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public History save(String from, String to, Double amount, Double sum, String token) {
        String username = jwtTokenProvider.getUsername(token);
        User user = userRepository.findByEmail(username).orElse(null);
        return historyRepository.save(new History().setFromVal(from)
                          .setToVal(to)
                          .setAmount(amount)
                          .setSum(sum)
                          .setUser(user));
    }

    public List<HistoryDto> getAll(String token) {
        String username = jwtTokenProvider.getUsername(token);
        User user = userRepository.findByEmail(username).orElse(null);
        List<History> list = historyRepository.findByUser(user);
        return list.stream().map(x -> HistoryDto.fromHistory(x)).collect(Collectors.toList());
    }

}
