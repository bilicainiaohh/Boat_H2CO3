/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.opengl;

import org.lwjgl.system.*;

import static org.lwjgl.system.Checks.*;

/**
 * Native bindings to the <a target="_blank" href="https://www.khronos.org/registry/OpenGL/extensions/EXT/EXT_external_objects_fd.txt">EXT_semaphore_fd</a> extension.
 *
 * <p>Building upon the OpenGL memory object and semaphore framework defined in <a target="_blank" href="https://www.khronos.org/registry/OpenGL/extensions/EXT/EXT_external_objects.txt">EXT_external_objects</a> this extension enables an OpenGL
 * application to import a memory object or semaphore from POSIX file descriptor external handles.</p>
 *
 * <p>Requires {@link EXTSemaphore EXT_semaphore} and {@link ARBTextureStorage ARB_texture_storage} or a version of OpenGL that incorporates it.</p>
 */
public class EXTSemaphoreFD {

    /** Accepted by the {@code handleType} parameter of {@link #glImportSemaphoreFdEXT ImportSemaphoreFdEXT}. */
    public static final int GL_HANDLE_TYPE_OPAQUE_FD_EXT = 0x9586;

    static {
        GL.initialize();
    }

    protected EXTSemaphoreFD() {
        throw new UnsupportedOperationException();
    }

    static boolean isAvailable(GLCapabilities caps) {
        return checkFunctions(
            caps.glImportSemaphoreFdEXT
        );
    }

    // --- [ glImportSemaphoreFdEXT ] ---

    public static native void glImportSemaphoreFdEXT(@NativeType("GLuint") int semaphore, @NativeType("GLenum") int handleType, @NativeType("GLint") int fd);

}