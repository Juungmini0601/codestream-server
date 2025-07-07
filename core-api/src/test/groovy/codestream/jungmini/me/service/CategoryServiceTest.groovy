package codestream.jungmini.me.service

import codestream.jungmini.me.database.repository.CategoryRepository
import codestream.jungmini.me.model.Category
import codestream.jungmini.me.support.error.CustomException
import codestream.jungmini.me.support.error.ErrorType
import spock.lang.Specification

class CategoryServiceTest extends Specification {

    CategoryRepository categoryRepository = Stub()
    
    CategoryService categoryService = new CategoryService(categoryRepository)
    
    def "카테고리 생성에 성공한다"() {
        given:
        def name = "Java"
        def category = Category.from(name)
        
        categoryRepository.existsByName(name) >> false
        categoryRepository.save(_ as Category) >> category
        
        when:
        def result = categoryService.createCategory(name)
        
        then:
        result.name == name
    }
    
    def "중복된 카테고리명으로 생성시 예외를 발생시킨다"() {
        given:
        def name = "Java"
        
        categoryRepository.existsByName(name) >> true
        
        when:
        categoryService.createCategory(name)
        
        then:
        def exception = thrown(CustomException)
        exception.errorType == ErrorType.VALIDATION_ERROR
    }
    
    def "카테고리 수정에 성공한다"() {
        given:
        def categoryId = 1L
        def name = "Spring"
        def existingCategory = Category.from("Java")
        
        categoryRepository.existsByName(name) >> false
        categoryRepository.findById(categoryId) >> Optional.of(existingCategory)
        categoryRepository.update(_ as Category) >> {}
        
        when:
        def result = categoryService.updateCategory(categoryId, name)
        
        then:
        result.name == name
    }
    
    def "중복된 카테고리명으로 수정시 예외를 발생시킨다"() {
        given:
        def categoryId = 1L
        def name = "Java"
        
        categoryRepository.existsByName(name) >> true
        
        when:
        categoryService.updateCategory(categoryId, name)
        
        then:
        def exception = thrown(CustomException)
        exception.errorType == ErrorType.VALIDATION_ERROR
    }
    
    def "존재하지 않는 카테고리 수정시 예외를 발생시킨다"() {
        given:
        def categoryId = 1L
        def name = "Spring"
        
        categoryRepository.existsByName(name) >> false
        categoryRepository.findById(categoryId) >> Optional.empty()
        
        when:
        categoryService.updateCategory(categoryId, name)
        
        then:
        def exception = thrown(CustomException)
        exception.errorType == ErrorType.VALIDATION_ERROR
    }
}
