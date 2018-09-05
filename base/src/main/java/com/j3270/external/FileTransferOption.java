/*
 * Copyright (C) 2016 Daniel Yokomizo
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.j3270.external;

import static com.j3270.base.Extras.enumValueOfIgnoreCase;

import com.j3270.base.FileTransfer;

/**
 * @see http://x3270.bgp.nu/Unix/s3270-man.html#File-Transfer
 * @author Daniel Yokomizo
 */
enum FileTransferOption {
	Direction {
		@Override
		public void set(FileTransfer transfer, String value) {
			transfer.direction(enumValueOfIgnoreCase(FileTransfer.Direction.class, value));
		}
	},
	HostFile(true) {
		@Override
		public void set(FileTransfer transfer, String value) {}
	},
	LocalFile(true) {
		@Override
		public void set(FileTransfer transfer, String value) {}
	},
	Host {
		@Override
		public void set(FileTransfer transfer, String value) {
			transfer.host(enumValueOfIgnoreCase(FileTransfer.Host.class, value));
		}
	},
	Mode {
		@Override
		public void set(FileTransfer transfer, String value) {
			transfer.mode(enumValueOfIgnoreCase(FileTransfer.Mode.class, value));
		}
	},
	Cr {
		@Override
		public void set(FileTransfer transfer, String value) {
			transfer.cr(enumValueOfIgnoreCase(FileTransfer.Cr.class, value));
		}
	},
	Remap {
		@Override
		public void set(FileTransfer transfer, String value) {
			transfer.remap(enumValueOfIgnoreCase(FileTransfer.Remap.class, value));
		}
	},
	Exist {
		@Override
		public void set(FileTransfer transfer, String value) {
			transfer.exist(enumValueOfIgnoreCase(FileTransfer.Exist.class, value));
		}
	},
	Recfm {
		@Override
		public void set(FileTransfer transfer, String value) {
			transfer.recfm(enumValueOfIgnoreCase(FileTransfer.Recfm.class, value));
		}
	},
	Lrecl {
		@Override
		public void set(FileTransfer transfer, String value) {
			transfer.lrecl(Long.parseLong(value));
		}
	},
	Blksize {
		@Override
		public void set(FileTransfer transfer, String value) {
			transfer.blksize(Long.parseLong(value));
		}
	},
	Allocation {
		@Override
		public void set(FileTransfer transfer, String value) {
			transfer.allocation(enumValueOfIgnoreCase(FileTransfer.Allocation.class, value));
		}
	},
	PrimarySpace {
		@Override
		public void set(FileTransfer transfer, String value) {
			transfer.primarySpace(Long.parseLong(value));
		}
	},
	SecondarySpace {
		@Override
		public void set(FileTransfer transfer, String value) {
			transfer.secondarySpace(Long.parseLong(value));
		}
	},
	Avblock {
		@Override
		public void set(FileTransfer transfer, String value) {
			transfer.avblock(Long.parseLong(value));
		}
	},
	BufferSize {
		@Override
		public void set(FileTransfer transfer, String value) {
			transfer.bufferSize(Integer.parseInt(value));
		}
	}, //
	;
	private final boolean required;

	private FileTransferOption() {
		this(false);
	}

	private FileTransferOption(boolean required) {
		this.required = required;
	}

	public abstract void set(FileTransfer transfer, String value);

	public boolean isRequired() {
		return required;
	}

	private Object readResolve() {
		return FileTransferOption.valueOf(name());
	}

	public static FileTransferOption fileTransferOption(String name) {
		return enumValueOfIgnoreCase(FileTransferOption.class, name);
	}

}
