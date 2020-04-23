package com.summer.util;//package com.summer.util;
//
//import org.apache.taglibs.standard.lang.jstl.Logger;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//public class TokenManager {
//
//
////token公共池 ，使用线程安全的map
//private static final ConcurrentHashMap<String, TokenDTO> tokenPool = new ConcurrentHashMap<>();
//
//
////过期时长
//private static final long expireDuration = 24*60*60*1000L;
//
///**
// * 生成token 并放到池中
// * @param user
// * @return
// */
//public TokenDTO makeToken(UserDTO user) {
//
//    //主要是跟user和当前时间戳生成token
//    String tokenString = MD5.Hash(user.toString()+System.currentTimeMillis());
//    TokenDTO tokenDTO = new TokenDTO();
//    tokenDTO.setExpire(System.currentTimeMillis()+expireDuration);
//    tokenDTO.setToken(tokenString);
//    tokenDTO.setUser(user);
//
//    //存放到池中
//    tokenPool.put(tokenString,tokenDTO);
//    return tokenDTO;
//}
//
///**
// * 获取token
// * @param token
// * @return
// */
//public TokenDTO query(String token) {
//    return tokenPool.get(token);
//}
//
///**
// * 删除token
// * @param token
// * @param tokenDTO
// */
//public void deleteToken(String token, TokenDTO tokenDTO) {
//    tokenPool.remove(token);
//}
//
///**
// * 定时清理无效token
// */
//@Scheduled(initialDelay = expireDuration,fixedDelay = expireDuration )
//private void cleanToken() {
//    logger.info("cleanToken","开始clean token");
//
//    tokenPool.forEach((key, value) -> {
//        if (value.getExpire() <= System.currentTimeMillis()) {
//            tokenPool.remove(key,value);
//        }
//    });
//}
//
//}