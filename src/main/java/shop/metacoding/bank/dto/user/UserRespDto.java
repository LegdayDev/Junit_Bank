package shop.metacoding.bank.dto.user;

import lombok.Data;
import shop.metacoding.bank.domain.user.User;

public class UserRespDto {
    @Data
    public static class JoinRespDto{
        private Long id;
        private String username;
        private String fullName;

        public JoinRespDto(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.fullName = user.getFullName();
        }
    }
}
