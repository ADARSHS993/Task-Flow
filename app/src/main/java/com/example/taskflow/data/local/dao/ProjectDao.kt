package com.example.taskflow.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.taskflow.data.local.entity.ProjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    @Query(""" SELECT * FROM projects ORDER BY createAt DESC""")
    fun getAllProjects(): Flow<List<ProjectEntity>>

    @Query(""" SELECT * FROM projects ORDER BY createAt DESC LIMIT 4""")
    fun getRecentProjects(): Flow<List<ProjectEntity>>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateProject(project: ProjectEntity)

    @Delete
    suspend fun deleteProject(project: ProjectEntity)

    @Query("""SELECT * FROM projects WHERE id = :projectId LIMIT 1 """)
    suspend fun getProjectById(projectId: String): ProjectEntity?

}