/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 * MACHINE GENERATED FILE, DO NOT EDIT
 */
package org.lwjgl.opengl;

import org.lwjgl.system.*;

import static org.lwjgl.system.Checks.*;

/**
 * Native bindings to the <a target="_blank" href="https://www.khronos.org/registry/OpenGL/extensions/EXT/EXT_external_objects_fd.txt">EXT_memory_object_fd</a> extension.
 *
 * <p>Building upon the OpenGL memory object and semaphore framework defined in <a target="_blank" href="https://www.khronos.org/registry/OpenGL/extensions/EXT/EXT_external_objects.txt">EXT_external_objects</a> this extension enables an OpenGL
 * application to import a memory object or semaphore from POSIX file descriptor external handles.</p>
 *
 * <p>Requires {@link EXTMemoryObject EXT_memory_object} and {@link ARBTextureStorage ARB_texture_storage} or a version of OpenGL that incorporates it.</p>
 */
public class EXTMemoryObjectFD {

    /** Accepted by the {@code handleType} parameter of {@link #glImportMemoryFdEXT ImportMemoryFdEXT} */
    public static final int GL_HANDLE_TYPE_OPAQUE_FD_EXT = 0x9586;

    static {
        GL.initialize();
    }

    protected EXTMemoryObjectFD() {
        throw new UnsupportedOperationException();
    }

    static boolean isAvailable(GLCapabilities caps) {
        return checkFunctions(
            caps.glImportMemoryFdEXT
        );
    }

    // --- [ glImportMemoryFdEXT ] ---

    public static native void glImportMemoryFdEXT(@NativeType("GLuint") int memory, @NativeType("GLuint64") long size, @NativeType("GLenum") int handleType, @NativeType("GLint") int fd);

}