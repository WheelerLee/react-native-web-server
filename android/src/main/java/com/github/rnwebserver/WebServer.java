package com.github.rnwebserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import fi.iki.elonen.NanoHTTPD;

public class WebServer extends NanoHTTPD {
    private static final String TAG = "WebServer";

    private List<HandlerOption> handlers = new ArrayList<>();

    public WebServer(List<HandlerOption> handlers) {
        // 使用端口0，让系统随机分配一个未使用的端口
        super(0);
        this.handlers = handlers;
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        File target = null;

        for (HandlerOption handler : handlers) {
            String key = handler.getPrefix();
            if (uri.startsWith(key)) {
                target = new File(handler.getPath() + (key.equals("/") ? uri : uri.substring(key.length())));
            }
        }

        if (target == null || !target.exists()) {
            return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "404 Not Found");
        }

        if (target.isDirectory()) {
            File indexFile = new File(target, "index.html");
            if (indexFile.exists()) {
                target = indexFile;
            } else {
                // 生成文件列表 HTML
                String html = generateDirectoryListing(target, uri);
                return newFixedLengthResponse(Response.Status.OK, "text/html", html);
            }
        }

        try {
            return newChunkedResponse(Response.Status.OK, getMimeTypeForFile(target.getName()), new FileInputStream(target));
        } catch (IOException e) {
            e.printStackTrace();
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/plain", "500 Internal Server Error");
        }
    }

    private String generateDirectoryListing(File directory, String uri) {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></head><body><ul>");

        File[] files = directory.listFiles();
        if (files != null) {
            // 排序：目录在前，文件在后，各自按名称排序
            Arrays.sort(files, (f1, f2) -> {
                if (f1.isDirectory() && !f2.isDirectory()) {
                    return -1;
                } else if (!f1.isDirectory() && f2.isDirectory()) {
                    return 1;
                } else {
                    return f1.getName().compareToIgnoreCase(f2.getName());
                }
            });

            for (File file : files) {
                String fileName = file.getName();
                String fileUri = uri.endsWith("/") ? uri + fileName : uri + "/" + fileName;
                String displayName = escapeHtml(fileName);
                
                html.append("<li><a href=\"").append(escapeHtml(fileUri)).append("\">");
                html.append(displayName);
                html.append("</a></li>");
            }
        }

        html.append("</ul></body></html>");
        return html.toString();
    }

    private String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }

    @Override
    public void stop() {
        super.stop();
        handlers.clear();
    }
}
