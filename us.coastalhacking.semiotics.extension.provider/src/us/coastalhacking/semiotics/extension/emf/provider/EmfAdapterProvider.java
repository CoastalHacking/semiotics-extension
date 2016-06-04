
package us.coastalhacking.semiotics.extension.emf.provider;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.osgi.dto.DTO;
import org.osgi.service.component.annotations.Component;

import io.opensemantics.semiotics.model.assessment.AssessmentFactory;
import io.opensemantics.semiotics.model.assessment.AssessmentPackage;
import io.opensemantics.semiotics.model.assessment.Http;
import us.coastalhacking.semiotics.extension.emf.api.EmfAdapter;

/**
 * @author jonpasski
 *
 */
@Component(name="us.coastalhacking.semiotics.extension.emf")
public class EmfAdapterProvider implements EmfAdapter {

	/* (non-Javadoc)
	 * @see us.coastalhacking.semiotics.extension.emf.api.EmfAdapter#toHttp(org.osgi.dto.DTO)
	 */
	@Override
	public <D extends DTO> Http toHttp(D dto) {
		Http http = AssessmentFactory.eINSTANCE.createHttp();
		adapt(dto, http);
		return http;
	}
	
	// TODO : cache
	private <E extends EObject, D extends DTO> void adapt(D dto, E eObject) {
			try {
				Class<?> c = dto.getClass();
				if (!Modifier.isPublic(c.getModifiers())) {
					// TODO log? maybe debug
					return;
				}
				// TODO quadratic :(
				for (Field f : c.getDeclaredFields()) {
					if (!Modifier.isPublic(f.getModifiers()))
						continue;

					EList<EAttribute> attributes = eObject.eClass().getEAllAttributes();
					for (EAttribute att : attributes) {
						if (att.getName().equals(f.getName())) {
							try {
								// TODO check types? or accept the runtime exception?
								eObject.eSet(att, f.get(dto));
							} catch (IllegalArgumentException | IllegalAccessException e) {
								// TODO Auto-generated catch block debug log
								e.printStackTrace();
							}
						}
					}
				}
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
}
