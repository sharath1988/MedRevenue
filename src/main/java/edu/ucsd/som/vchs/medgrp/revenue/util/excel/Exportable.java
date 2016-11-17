package edu.ucsd.som.vchs.medgrp.revenue.util.excel;

import java.io.IOException;
import java.io.OutputStream;

public interface Exportable {
	String getFilename();
	String getContentType();
	void write(OutputStream outputStream) throws IOException;
}
