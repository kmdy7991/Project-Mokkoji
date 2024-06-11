//package com.ssafy.mokkoji.common.config;
//
//import org.apache.catalina.Context;
//import org.apache.catalina.connector.Connector;
//import org.apache.tomcat.util.descriptor.web.SecurityCollection;
//import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
//import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
//import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class SslConfiguration {
//    @Bean
//    public ServletWebServerFactory servletWebServerFactory() {
//        TomcatServletWebServerFactory tomcatServletWebServerFactory = new TomcatServletWebServerFactory() {
//            /*
//             * ssl 설정
//             * 아래 method를 통해 접근 권한을 설정
//             * addPattern으로 특정 uri를 매핑할 수 있음 *은 모든 요청을 https로 매핑함
//             * */
//            @Override
//            protected void postProcessContext(Context context) {
//                SecurityConstraint securityConstraint = new SecurityConstraint();
//                /*
//                 * 하위 내용 중 하나로 설정
//                 * NONE : 디폴트 값으로 데이터 보호를 하지 않겠다는 의미
//                 * INTEGRAL : 전송 중 데이터가 변경되지 않음을 보장 - 무결성
//                 * CONFIDENTIAL : 전송 중 데이터를 누구도 훔쳐보지 않았음을 보장 - 기밀성
//                 * */
//                securityConstraint.setUserConstraint("CONFIDENTIAL");
//
//                SecurityCollection securityCollection = new SecurityCollection();
//                securityCollection.addPattern("/*");
//
//                securityConstraint.addCollection(securityCollection);
//
//                // context에 상위 내용을 추가하여 TomcatServletServerFactory에 설정한다
//                context.addConstraint(securityConstraint);
//            }
//        };
//
//        tomcatServletWebServerFactory.addAdditionalTomcatConnectors(createStandardConnector());
////        Set<String> set = new HashSet<>();
////        set.
//
//        return tomcatServletWebServerFactory;
//
//    }
//
//    private Connector createStandardConnector() {
//        /*
//         * Connect객체는 tomcat catalina와 nio부분 참조
//         *
//         * tomcat 구성
//         * coyote -> catalina -> jasper
//         *
//         * catalina에서는 servlet환경을 구성함
//         *
//         * secure는 https일 경우 true, 아닐때 false
//         * 해당 메서드에서 port를 http포트로 설정 했으므로 false
//         *
//         * scheme은 프로토콜 명시
//         * */
//        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//        connector.setScheme("http");
//        connector.setPort(8080);
//
//        connector.setSecure(false);
//
//        // http -> https
//        connector.setRedirectPort(443);
//        return connector;
//    }
//}