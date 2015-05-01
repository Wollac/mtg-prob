package probability.csv;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import probability.util.Attribute;
import probability.util.AttributeHolder;

import com.opencsv.CSVReader;

abstract class AbstractCSVParser<T> {

	private final CSVReader _reader;

	private final Set<Attribute<?>> _attributes;

	private Map<Attribute<?>, Integer> _attribute2colnum;

	public AbstractCSVParser(Reader reader) throws IOException {
		_reader = new CSVReader(new LineCommentReader(reader));
		
		_attributes = new HashSet<>();
	}

	protected void addAttribute(Attribute<?> attribute) {
		_attributes.add(attribute);
	}

	public List<T> readAll() throws IOException {

		if (_attributes.isEmpty()) {
			throw new IllegalStateException("At least one attribute is needed.");
		}

		readHeader();

		List<T> instances = new ArrayList<>();

		String[] nextLine;
		while ((nextLine = _reader.readNext()) != null) {
			Row row = parseLine(nextLine);

			instances.addAll(createInstance(row));
		}

		return instances;
	}

	protected abstract Collection<T> createInstance(Row row);

	private Row parseLine(String[] nextLine) {

		AttributeHolder attributeHolder = new AttributeHolder();

		for (Entry<Attribute<?>, Integer> element : _attribute2colnum
				.entrySet()) {
			attributeHolder.setParsedAttributeValue(element.getKey(),
					nextLine[element.getValue()]);
		}

		return attributeHolder;
	}

	private void readHeader() throws IOException {

		_attribute2colnum = new HashMap<>();

		List<String> line = Arrays.asList(_reader.readNext());

		for (Attribute<?> attribute : _attributes) {
			int index = line.indexOf(attribute.getName());

			if (index >= 0) {
				_attribute2colnum.put(attribute, index);
			}
		}
	}

}
