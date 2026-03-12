package com.joseag.coldroommonitor.api.mappers;

import com.joseag.coldroommonitor.api.dto.request.MeasurementCreateRequest;
import com.joseag.coldroommonitor.api.dto.request.MeasurementSearchRequest;
import com.joseag.coldroommonitor.api.dto.response.ColdRoomMeasurementResponse;
import com.joseag.coldroommonitor.api.dto.response.MeasurementItemResponse;
import com.joseag.coldroommonitor.api.dto.response.SensorMeasurementResponse;
import com.joseag.coldroommonitor.application.command.CreateMeasurementCommand;
import com.joseag.coldroommonitor.application.command.MeasurementItemCommand;
import com.joseag.coldroommonitor.application.command.SearchMeasurementCommand;
import com.joseag.coldroommonitor.domain.model.Measurement;
import com.joseag.coldroommonitor.domain.model.SensorDevice;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MeasurementMapper {

    public CreateMeasurementCommand toCommand(MeasurementCreateRequest request){
        return new CreateMeasurementCommand(
                request.sensorId(),
                request.measurements().stream().map(item ->
                        new MeasurementItemCommand(item.currentValue(), item.measuredAt())
                ).toList()
        );
    }

    public SearchMeasurementCommand toCommand(MeasurementSearchRequest request){
        return new SearchMeasurementCommand(
          request.from(),
          request.to()
        );
    }



    public SensorMeasurementResponse toSensorResponse(Long sensorId, List<Measurement> measurement){
        return new SensorMeasurementResponse(
          sensorId,
          measurement.stream().map(item ->
                  new MeasurementItemResponse(item.getValue(), item.getMeasuredAt())
          ).toList()
        );
    }

    public Page<MeasurementItemResponse> toSensorResponse(Page<Measurement> measurement){
        return measurement.map(item ->
                new MeasurementItemResponse(item.getValue(), item.getMeasuredAt()));
    }
}
