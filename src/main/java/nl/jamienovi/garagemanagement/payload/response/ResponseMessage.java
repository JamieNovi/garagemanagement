package nl.jamienovi.garagemanagement.payload.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * De FileController gebruikt deze klasse om informatie/notificaties te sturen
 * via HTTP responses
 */

@AllArgsConstructor
@Getter
@Setter
public class ResponseMessage {

	private String message;

}
