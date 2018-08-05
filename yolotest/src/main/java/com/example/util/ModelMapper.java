package com.example.util;


import com.example.entity.Diary;
import com.example.entity.User;
import com.example.payload.DiaryResponse;
import com.example.payload.UserSummary;
//mapping the Diary entity to a DiaryResponse payload
public class ModelMapper {
	public static DiaryResponse mapDiaryToDiaryResponse(Diary diary, User creator) {
		DiaryResponse diaryResponse = new DiaryResponse();
		diaryResponse.setId(diary.getId());
		diaryResponse.setText(diary.getText());
		diaryResponse.setCreationDateTime(diary.getCreatedAt());

		UserSummary creatorSummary = new UserSummary(creator.getUsername());
		diaryResponse.setCreatedBy(creatorSummary);

		return diaryResponse;

	}

}
