package com.example.clean.entry.feature_auth.data.source.local

import app.cash.sqldelight.Query
import app.cash.sqldelight.Transacter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlPreparedStatement

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return WasmSqlDriver
    }
}


/**
 * A no-op implementation of the SqlDriver for the WebAssembly target.
 * This allows the application to compile but does not provide any actual
 * database functionality in the browser. A production web app would replace
 * this with a driver that uses a browser-specific storage API like IndexedDB.
 */
object WasmSqlDriver : SqlDriver {
    override fun <R> executeQuery(
        identifier: Int?,
        sql: String,
        mapper: (SqlCursor) -> QueryResult<R>,
        parameters: Int,
        binders: (SqlPreparedStatement.() -> Unit)?
    ): QueryResult<R> {
        // Returns a successful result with an empty value
        return QueryResult.AsyncValue {
            mapper(EmptyCursor()).value
        }
    }

    override fun execute(
        identifier: Int?,
        sql: String,
        parameters: Int,
        binders: (SqlPreparedStatement.() -> Unit)?
    ): QueryResult<Long> {
        // Returns a successful result of 0 rows affected
        return QueryResult.AsyncValue { 0L }
    }

    // A fake cursor that always reports being empty.
    private class EmptyCursor : SqlCursor {
        override fun getBytes(index: Int): ByteArray? = null
        override fun getDouble(index: Int): Double? = null
        override fun getBoolean(index: Int): Boolean? = null
        override fun getLong(index: Int): Long? = null
        override fun getString(index: Int): String? = null
        override fun next(): QueryResult<Boolean> = QueryResult.AsyncValue { false }
    }

    // The rest of the functions are no-op or return default values.
    override fun newTransaction(): QueryResult<Transacter.Transaction> {
        return QueryResult.AsyncValue {
            object : Transacter.Transaction() {
                override val enclosingTransaction: Transacter.Transaction? = null
                override fun endTransaction(successful: Boolean): QueryResult<Unit> =
                    QueryResult.AsyncValue {}
            }
        }
    }

    override fun currentTransaction(): Transacter.Transaction? = null
    override fun addListener(vararg queryKeys: String, listener: Query.Listener) {}
    override fun removeListener(vararg queryKeys: String, listener: Query.Listener) {}
    override fun notifyListeners(vararg queryKeys: String) {}
    override fun close() {}
}