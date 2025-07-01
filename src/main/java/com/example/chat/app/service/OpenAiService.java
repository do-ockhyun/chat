package com.example.chat.app.service;

import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class OpenAiService {
    private static final String[] SONGS = {
        "# 제목: 봄날\n\n**본문:**\n보고 싶다 이렇게 말하니까 더 보고 싶다\n너희 사진을 보고 있어도 보고 싶다\n\n**출처:** 방탄소년단 - 봄날\n\n**의견:** 이 노래는 이별과 그리움을 담담하게 표현해서 많은 이들의 공감을 샀다.",
        "# 제목: 너의 의미\n\n**본문:**\n넌 나에게 의미가 되어\n내가 사는 이유가 되어\n\n**출처:** 산울림 - 너의 의미\n\n**의견:** 따뜻한 가사와 멜로디가 마음을 편안하게 해준다.",
        "# 제목: 사랑했지만\n\n**본문:**\n사랑했지만 그대를 사랑했지만\n난 내겐 어쩔 수 없는 친구라는 걸\n\n**출처:** 김광석 - 사랑했지만\n\n**의견:** 이별의 아픔을 진솔하게 담아낸 명곡이다.",
        "# 제목: 비상\n\n**본문:**\n이 세상 어디라도 난 두려움 없으니\n날아올라 저 하늘 위로\n\n**출처:** 임재범 - 비상\n\n**의견:** 희망과 용기를 주는 가사로 많은 이들에게 힘이 된다."
    };
    private final Random random = new Random();

    public String generateResponse(String userMessage) {
        int idx = random.nextInt(SONGS.length);
        return SONGS[idx];
    }
} 