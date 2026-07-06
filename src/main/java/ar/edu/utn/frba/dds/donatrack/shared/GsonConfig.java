package ar.edu.utn.frba.dds.donatrack.shared;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import io.javalin.json.JsonMapper;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.jetbrains.annotations.NotNull;

public class GsonConfig {

  private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
  private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ISO_LOCAL_DATE;

  private GsonConfig() {
  }

  public static Gson crear() {
    return new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class,
            (JsonSerializer<LocalDateTime>) (fecha, tipo, ctx) ->
                new JsonPrimitive(fecha.format(FORMATO_FECHA_HORA)))
        .registerTypeAdapter(LocalDateTime.class,
            (JsonDeserializer<LocalDateTime>) (json, tipo, ctx) ->
                LocalDateTime.parse(json.getAsString(), FORMATO_FECHA_HORA))
        .registerTypeAdapter(LocalDate.class,
            (JsonSerializer<LocalDate>) (fecha, tipo, ctx) ->
                new JsonPrimitive(fecha.format(FORMATO_FECHA)))
        .registerTypeAdapter(LocalDate.class,
            (JsonDeserializer<LocalDate>) (json, tipo, ctx) ->
                LocalDate.parse(json.getAsString(), FORMATO_FECHA))
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .create();
  }

  public static JsonMapper jsonMapper() {
    Gson gson = crear();
    return new JsonMapper() {
      @Override
      public @NotNull String toJsonString(@NotNull Object obj, @NotNull Type type) {
        return gson.toJson(obj, type);
      }

      @Override
      public <T> @NotNull T fromJsonString(@NotNull String json, @NotNull Type targetType) {
        return gson.fromJson(json, targetType);
      }
    };
  }

}