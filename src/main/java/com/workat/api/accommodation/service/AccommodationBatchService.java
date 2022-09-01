package com.workat.api.accommodation.service;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.workat.api.accommodation.dto.CsvAccommodationDto;
import com.workat.common.exception.NotFoundException;
import com.workat.common.util.FileReadUtils;
import com.workat.domain.accommodation.RegionType;
import com.workat.domain.accommodation.entity.Accommodation;
import com.workat.domain.accommodation.entity.AccommodationInfo;
import com.workat.domain.accommodation.repository.AccommodationInfoRepository;
import com.workat.domain.accommodation.repository.AccommodationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccommodationBatchService {

	private final String ROOT_DIRECTORY_PATH = "/csv/accommodation/";

	private final AccommodationRepository accommodationRepository;

	private final AccommodationInfoRepository accommodationInfoRepository;

	public void initAccommodation() {
		Arrays.stream(RegionType.values())
			.forEach(regionType -> {
				String filePath = ROOT_DIRECTORY_PATH + regionType.getValue().toLowerCase();

				List<File> childFiles = accommodationChildFileUrls(filePath);

				if (childFiles.isEmpty()) {
					log.info(filePath + " child files is not exist");
					return;
				}

				childFiles.forEach(file -> readCsv(regionType, file));
			});
	}

	private void readCsv(RegionType regionType, File file) {
		List<Accommodation> result = FileReadUtils.readCSV(file).stream()
			.map(line -> {
				CsvAccommodationDto dto = CsvAccommodationDto.of(regionType, line);

				Accommodation accommodation = accommodationRepository.findByName(dto.getName())
					.orElse(Accommodation.of());
				accommodation.update(dto);
				accommodationRepository.save(accommodation);

				List<AccommodationInfo> accommodationInfoList = dto.getInfoTags().stream()
					.map(info -> AccommodationInfo.of(info, accommodation))
					.collect(Collectors.toList());

				accommodationInfoRepository.saveAll(accommodationInfoList);

				return accommodation;
			})
			.collect(Collectors.toList());

		accommodationRepository.saveAll(result);
	}

	private List<File> accommodationChildFileUrls(String rootPath) {
		if (rootPath == null || rootPath.isBlank()) {
			throw new RuntimeException(rootPath + " must not be null or blank");
		}

		URL url = getClass().getResource(rootPath);

		if (url == null || url.getPath() == null) {
			log.info(rootPath + " is not exist");
			return Collections.emptyList();
		}

		File file = new File(url.getPath());

		if (!file.exists()) {
			log.info(rootPath + " is not exist");
			return Collections.emptyList();
		}

		File[] files = file.listFiles();
		if (files == null || files.length == 0) {
			log.info("child file is null or empty");
			return Collections.emptyList();
		}

		return List.of(files);
	}
}
