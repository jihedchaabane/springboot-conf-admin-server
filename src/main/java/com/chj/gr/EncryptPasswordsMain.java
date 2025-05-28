/** 
package com.chj.gr;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
public class EncryptPasswordsMain {
	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println("admin:admin=>" + encoder.encode("admin")); 	//==> admin
		System.out.println("user:user  =>" + encoder.encode("user"));	//==> user
	}
}
//admin:admin=>$2a$10$vJaSzy8EhH/HAB1UDaE42uFKu54JQlXqagqqz8pyGi7ZrAEmf.Opy
//user:user  =>$2a$10$S1eEZm5TRtev/WwUKQtm0uP5d/.g6ld14nTDuW3PAA.jLHiuutZWK
*/