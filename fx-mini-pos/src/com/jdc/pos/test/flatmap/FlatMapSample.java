package com.jdc.pos.test.flatmap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FlatMapSample {

	public static void main(String[] args) {
		
		Student s1 = new Student();
		s1.setName("Kyaw Kyaw");
		s1.addCourse("JavaSE");
		s1.addCourse("Spring");
		s1.addCourse("Kotlin");
		
		Student s2 = new Student();
		s2.setName("Mg Mg");
		s2.addCourse("JavaSE");
		s2.addCourse("JavaEE");
		
		List<Student> students = new ArrayList<Student>();
		students.add(s1);
		students.add(s2);
		
		List<String> result = students.stream()
			.map(stu -> stu.getCourses())
			.flatMap(courses -> courses.stream())
			.distinct()
			.collect(Collectors.toList());
		
		result.forEach(System.out::println);
	}
}
