package bsw.webfluxsampler.web.book

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingConstants
import org.mapstruct.ReportingPolicy

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
interface BookMapper {

    @Mapping(source = "name", target = "bookName")
    @Mapping(target = "copy", ignore = true)
    fun bookToResponse(book: Book): BookResponse

}