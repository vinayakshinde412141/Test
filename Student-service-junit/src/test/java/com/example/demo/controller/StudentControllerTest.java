package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.demo.model.Course;
import com.example.demo.service.StudentService;

@RunWith(SpringRunner.class)
@WebMvcTest(value=StudentController.class)
@WithMockUser
class StudentControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private StudentService studentService;
	
	Course mockCourse = new Course("Course1", "Spring", "10Steps",
			Arrays.asList("Learn Maven", "Import Project", "First Example",
					"Second Example"));
	
	String exampleCourseJson="{\"name\":\"Spring\",\"description\":\"10Steps\",\"steps\":[\"Learn Maven\",\"Import Project\",\"First Example\",\"Second Example\"]}";
	
	
	@Test
	public void retrieveDetailsForCourse() throws Exception {
		Mockito.when(studentService.retrieveCourse(Mockito.anyString(), Mockito.anyString())).thenReturn(mockCourse);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/students/Student1/courses/Course1").accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		System.out.println("Vinayak Result : >> "+result.getResponse());
		
		String expected = "{id:Course1,name:Spring,description:10Steps}";
		
		JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
		
	}
	
	@Test
	public void createStudentCourse() throws Exception {
		Course mockCourse = new Course("1", "Smallest Number", "1",
				Arrays.asList("1", "2", "3", "4"));
		
		
		Mockito.when(
				studentService.addCourse(Mockito.anyString(),
						Mockito.any(Course.class))).thenReturn(mockCourse);

		
		RequestBuilder requestBuilder=MockMvcRequestBuilders.post("/student/Student1/courses")
			.accept(MediaType.APPLICATION_JSON).content(exampleCourseJson)
			.contentType(MediaType.APPLICATION_JSON);
		
		
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
		
		assertEquals("http://localhost/students/Student1/courses/1", response.getHeader(HttpHeaders.LOCATION));
		
	}
}