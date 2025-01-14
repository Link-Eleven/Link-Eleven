package com.linkeleven.msa.interaction.infrastructure.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;

import com.linkeleven.msa.interaction.libs.exception.CustomException;
import com.linkeleven.msa.interaction.libs.exception.ErrorCode;

public class AvroSerializer<T extends SpecificRecordBase> {

	public byte[] serialize(T data) {
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
			SpecificDatumWriter<T> writer = new SpecificDatumWriter<>(data.getSchema());
			writer.write(data, encoder);
			encoder.flush();
			return outputStream.toByteArray();
 		} catch (IOException e) {
			throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR_TO_SERIALIZE);
		}
	}

	public T deserialize(byte[] data, Schema schema) {
		try {
			BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(data, null);
			SpecificDatumReader<T> reader = new SpecificDatumReader<>(schema);
			return reader.read(null, decoder);
		} catch (IOException e) {
			throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR_TO_DESERIALIZE);
		}
	}
}
