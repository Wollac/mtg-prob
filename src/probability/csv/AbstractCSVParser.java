package probability.csv;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import probability.util.Attribute;
import probability.util.Attribute.AttributeParseException;
import probability.util.AttributeHolder;

import com.opencsv.CSVReader;

public abstract class AbstractCSVParser<T> {

	private final CSVReader _reader;

	private final Map<Attribute<?>, Boolean> _attributes;

	private Map<Attribute<?>, Integer> _attribute2colnum;

	public AbstractCSVParser(Reader reader) throws IOException {
		_reader = new CSVReader(new LineCommentReader(reader));

		_attributes = new HashMap<>();
	}

	protected void addAttribute(Attribute<?> attribute, boolean mandatory) {
		_attributes.put(attribute, mandatory);
	}

	protected void addOptionalAttribute(Attribute<?> attribute) {
		addAttribute(attribute, false);
	}

	protected void addMandatoryAttribute(Attribute<?> attribute) {
		addAttribute(attribute, true);
	}

	public List<T> readAll() throws IOException, CvsParseException {

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

	private Row parseLine(String[] nextLine) throws CvsParseException {

		AttributeHolder attributeHolder = new AttributeHolder();

		try {
			for (Entry<Attribute<?>, Integer> element : _attribute2colnum
					.entrySet()) {
				String valueString = nextLine[element.getValue()].trim();

				attributeHolder.setParsedAttributeValue(element.getKey(),
						valueString);
			}
		} catch (AttributeParseException e) {
			throw new CvsParseException("error parsing csv file", e);
		}

		return attributeHolder;
	}

	private void readHeader() throws IOException, CvsParseException {

		_attribute2colnum = new HashMap<>();

		List<String> line = Arrays.asList(_reader.readNext());

		for (Attribute<?> attribute : _attributes.keySet()) {

			int index = line.indexOf(attribute.getName());

			if (index >= 0) {
				_attribute2colnum.put(attribute, index);
			} else if (isMandatory(attribute)) {

				throw new CvsParseException("error parsing csv file",
						new Throwable("Mandatory column " + attribute.getName()
								+ " not found"));
			}
		}
	}

	private boolean isMandatory(Attribute<?> attribute) {
		return _attributes.get(attribute);
	}

	public static class CvsParseException extends Exception {

		private static final long serialVersionUID = 1L;

		public CvsParseException(String str, Throwable cause) {
			super(str, cause);
		}

	}

}
