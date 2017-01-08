package org.carbon.web.core.request;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.carbon.component.annotation.Component;
import org.carbon.util.SimpleKeyValue;
import org.carbon.web.exception.RequestMappingException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Shota Oda 2016/10/12.
 */
@Component
public class MultipartFormKeyValueRequestMapper implements TypeSafeRequestMapper {

	@Override
	public <T> T map(HttpServletRequest request, Class<T> mapTo) {
		return null;
	}

	public Map<String, Object> map(HttpServletRequest request) {
		ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
		upload.setSizeMax(20 * 1024);
		upload.setFileSizeMax(10 * 1024);

		List<FileItem> items;
		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e) {
			throw new RequestMappingException("", e);
		}

		return items.stream().map(item -> {
			String key = item.getFieldName();
			if (item.isFormField()) {
				String value = item.getString();
				return new SimpleKeyValue(key, value);
			} else {
				return new SimpleKeyValue(key, item);
			}
		}).collect(Collectors.toMap(
				kv -> kv.getKey(),
				kv -> kv.getValue()
		));
	}
}
