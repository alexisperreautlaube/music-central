package com.apa.importer.services;

import com.apa.importer.dto.MediaDto;

public interface Importer<M extends MediaDto> {
    void doImport(M media);
}
