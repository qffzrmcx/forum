package com.qffz.service;

import com.qffz.exception.BusinessException;
import com.qffz.mapper.SectionMapper;
import com.qffz.pojo.Section;
import com.qffz.utils.StringUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectionService {
    private final SectionMapper sectionMapper;

    public SectionService(SectionMapper sectionMapper) {
        this.sectionMapper = sectionMapper;
    }

    public List<Section> list(boolean admin) {
        return admin ? sectionMapper.allList() : sectionMapper.enabledList();
    }

    public void save(Section section) {
        if (StringUtil.isBlank(section.getName())) {
            throw new BusinessException("板块名称不能为空");
        }
        if (section.getSort() == null) section.setSort(0);
        if (section.getStatus() == null) section.setStatus(1);
        if (section.getId() == null) {
            sectionMapper.insert(section);
        } else {
            sectionMapper.update(section);
        }
    }

    public void delete(Long id) {
        sectionMapper.delete(id);
    }
}
