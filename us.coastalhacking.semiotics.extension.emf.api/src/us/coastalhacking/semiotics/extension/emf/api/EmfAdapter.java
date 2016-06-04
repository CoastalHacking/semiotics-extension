package us.coastalhacking.semiotics.extension.emf.api;

import org.osgi.annotation.versioning.ProviderType;
import org.osgi.dto.DTO;

import io.opensemantics.semiotics.model.assessment.Http;

/**
 * <p>
 * Converts DTOs into known EMF objects
 * </p>
 * 
 * @see ProviderType
 * @since 1.0
 */
@ProviderType
public interface EmfAdapter {

	<D extends DTO> Http toHttp(D dto);

}
