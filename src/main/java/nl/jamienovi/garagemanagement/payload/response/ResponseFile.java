package nl.jamienovi.garagemanagement.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * De FileController gebruikt deze klasse om berichten te versturen via HTTP reponses.
 */

@Getter
@Setter
@AllArgsConstructor
public class ResponseFile {
    private String name;
    private String url;
    private String type;
    private long size;
}
