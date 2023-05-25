package mn.delivery.system.util;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.rekognition.model.DetectLabelsResponse;
import software.amazon.awssdk.services.rekognition.model.DetectModerationLabelsResponse;
import software.amazon.awssdk.services.rekognition.model.Label;
import software.amazon.awssdk.services.rekognition.model.ModerationLabel;

import java.util.Map;

@Slf4j
public class IdCardLabelChecker {

    private static final Map<String, Integer> idCardLabelTexts = Map.of(
            "Document", 1,
            "Driving License", 1,
            "Id Cards", 1
    );

    private static final Map<String, Integer> personLabelTexts = Map.of(
            "Person", 1,
            "Face", 1,
            "Head", 1
    );

    private static final Map<String, Integer> nudityLabelTexts = Map.of(
            "Nudity", 1,
            "Explicit Nudity", 1,
            "Graphic Female Nudity", 1,
            "Graphic Male Nudity", 1
    );

    public static boolean isIdCard(DetectLabelsResponse detectLabelsResponse) {
        boolean isIdCard = false;

        for (Label label : detectLabelsResponse.labels()) {
            if (idCardLabelTexts.containsKey(label.name())) {
                isIdCard = true;
                break;
            }
        }

        return isIdCard;
    }

    public static boolean isPerson(DetectLabelsResponse detectLabelsResponse) {
        boolean isPerson = false;

        for (Label label : detectLabelsResponse.labels()) {
            if (personLabelTexts.containsKey(label.name())) {
                isPerson = true;
                break;
            }
        }

        return isPerson;
    }

    public static boolean isNudity(DetectModerationLabelsResponse detectModerationLabelsResponse) {
        boolean isNudity = false;

        for (ModerationLabel label : detectModerationLabelsResponse.moderationLabels()) {
            if (nudityLabelTexts.containsKey(label.name())) {
                isNudity = true;
                break;
            }
        }

        return isNudity;
    }
}
