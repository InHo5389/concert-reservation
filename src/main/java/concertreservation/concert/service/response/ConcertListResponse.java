package concertreservation.concert.service.response;

import concertreservation.concert.entity.Concert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConcertListResponse {

    private List<ConcertInfo> concertInfos;

    public static ConcertListResponse from(List<Concert> concerts) {
        List<ConcertInfo> concertInfos = concerts.stream()
                .map(concert -> new ConcertInfo(concert.getId(), concert.getTitle(), concert.getName(), concert.getImageUrl()))
                .toList();
        return new ConcertListResponse(concertInfos);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConcertInfo {
        private Long id;
        private String title;
        private String name;
        private String imageUrl;
    }
}
