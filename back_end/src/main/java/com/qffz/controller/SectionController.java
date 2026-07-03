package com.qffz.controller;

import com.qffz.dto.Result;
import com.qffz.pojo.Section;
import com.qffz.service.SectionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/section")
public class SectionController {
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping("/list")
    public Result<List<Section>> list() {
        return Result.ok(sectionService.list(false));
    }
}
