package mk.ukim.finki.univds.unrelated;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.jena.ext.com.google.common.collect.Lists;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Helper builder. Not intended for any use case.
 */
@RequiredArgsConstructor
public class StaticQueryBuilder {

    @NonNull
    private String querySceleton;

    @NonNull
    private String bodyPart;

    private List<String> body = Lists.newArrayList();

    public StaticQueryBuilder appendBodyPart(String replacement) {
        String bodyPart = MessageFormat.format(this.bodyPart, replacement);
        body.add(bodyPart.concat("\n"));
        return this;
    }

    public StaticQueryBuilder appendOperation(Operations replacement) {
        body.add(
                  "\t"
                  .concat(replacement.toString())
                  .concat("\n")
                );
        return this;
    }

    public String build() {

        String lastElement = body.get(body.size() - 1).trim();
        boolean endsInOperation = Stream.of(Operations.values()).map(Enum::toString).anyMatch(operation -> {
            return operation.equalsIgnoreCase(lastElement);
        });

        if (endsInOperation) {
            body.remove(body.size() - 1);
        }

        String bodyToString = body.stream().collect(Collectors.joining());

        String result = MessageFormat.format(querySceleton, bodyToString);
        body = Lists.newArrayList();
        return result;
    }

    public enum Operations {
        UNION,
        MINUS;
    }

}
