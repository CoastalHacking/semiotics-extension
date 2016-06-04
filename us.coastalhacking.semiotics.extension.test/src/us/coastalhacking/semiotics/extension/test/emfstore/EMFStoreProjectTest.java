package us.coastalhacking.semiotics.extension.test.emfstore;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.emfstore.client.ESLocalProject;
import org.eclipse.emf.emfstore.client.ESWorkspace;
import org.eclipse.emf.emfstore.client.ESWorkspaceProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import io.opensemantics.semiotics.model.assessment.Assessment;
import io.opensemantics.semiotics.model.assessment.AssessmentFactory;
import io.opensemantics.semiotics.model.assessment.Http;

@RunWith(MockitoJUnitRunner.class)
public class EMFStoreProjectTest {
	
	private ESWorkspace workspace;
	private ESLocalProject project;

	@Before
	public void setup() {
		workspace = ESWorkspaceProvider.INSTANCE.getWorkspace();
		project = workspace.createLocalProject("getmodelelementstest");
		project.getModelElements().clear();
	}
	/*
	 * Can multiple EObject types be added to Project.getModelElements()?
	 * For example, can a bunch of Sink objects be added?
	 */
	@Test
	public void testProjectGetModelElements() throws Exception {
		final EList<EObject> root = project.getModelElements();
		Assert.assertTrue(root.isEmpty());
		final Assessment assessment = AssessmentFactory.eINSTANCE.createAssessment();		
		root.add(assessment);
		final Http http = AssessmentFactory.eINSTANCE.createHttp();
		root.add(http);
		Assert.assertEquals(2, root.size());
	}
}
