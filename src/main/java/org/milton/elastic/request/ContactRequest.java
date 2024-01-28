package org.milton.elastic.request;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 * @author MILTON
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ContactRequest {

	private String id;

	private BigInteger userId;

	private String firstName;

	private String lastName;

	private String number;

	private String email;

	private String address;

	private Byte status;

}
